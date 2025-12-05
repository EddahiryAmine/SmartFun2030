import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';

import { MatchService } from '../../services/match.service';
import { StadiumService } from '../../services/stadium.service';

import { Match, MatchPhase } from '../../models/match.model';
import { Stadium } from '../../models/stadium.model';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { TicketService } from '../../services/ticket.service';

const PHASE_ORDER: Record<MatchPhase, number> = {
  GROUP: 1,
  ROUND_OF_16: 2,
  QUARTER_FINAL: 3,
  SEMI_FINAL: 4,
  THIRD_PLACE: 5,
  FINAL: 6
};

@Component({
  selector: 'app-matches-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './matches-list.component.html',
    styleUrls: ['./matches-list.component.css']
})
export class MatchesListComponent implements OnInit {

  matches: Match[] = [];
  filteredMatches: Match[] = [];

  loading = false;
  errorMessage = '';

  
  stadiumById: Record<string, Stadium> = {};

  selectedStatus = 'ALL';    
  selectedCountry = 'ALL';
  selectedStadium = 'ALL';
  selectedPhase: MatchPhase | 'ALL' = 'ALL';
  searchTeam = '';
  

 
  constructor(
  private matchService: MatchService,
  private stadiumService: StadiumService,
  private auth: AuthService,
  private ticketService: TicketService,
  private router: Router
) {}


  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.errorMessage = '';

    forkJoin({
      matches: this.matchService.getAllMatches(),
      stadiums: this.stadiumService.getAllStadiums()
    }).subscribe({
      next: ({ matches, stadiums }) => {
        this.stadiumById = {};
        stadiums.forEach(st => {
          if (st.id) {
            this.stadiumById[st.id] = st;
          }
        });

        this.matches = [...matches].sort((a, b) => {
          const phaseA: MatchPhase = (a.phase as MatchPhase) || 'GROUP';
          const phaseB: MatchPhase = (b.phase as MatchPhase) || 'GROUP';

          const orderA = PHASE_ORDER[phaseA];
          const orderB = PHASE_ORDER[phaseB];

          if (orderA !== orderB) {
            return orderA - orderB;
          }

          return new Date(a.kickoff).getTime() - new Date(b.kickoff).getTime();
        });

        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erreur lors du chargement des matchs/stades.';
        this.loading = false;
      }
    });
  }

  get countries(): string[] {
    const set = new Set<string>();
    Object.values(this.stadiumById).forEach(st => {
      if (st.country) set.add(st.country);
    });
    return Array.from(set);
  }

  get stadiums(): string[] {
    const set = new Set<string>();
    Object.values(this.stadiumById).forEach(st => {
      if (st.name) set.add(st.name);
    });
    return Array.from(set);
  }





   addFavorite(matchId: string) {
  if (!this.auth.isLoggedIn()) {
    this.router.navigate(['/login']);
    return;
  }

  this.ticketService.addFavorite(matchId, this.auth.getUserId()!).subscribe({
    next: () => alert('Ajouté aux favoris !'),
    error: () => alert('Erreur ajout favori')
  });
}

addToCart(matchId: string) {
  if (!this.auth.isLoggedIn()) {
    this.router.navigate(['/login']);
    return;
  }

  this.ticketService.addToCart({
    matchId,
    category: 'NORMAL',
    quantity: 1
  }, this.auth.getUserId()!).subscribe({
    next: () => alert('Ajouté au panier !'),
    error: () => alert('Erreur ajout panier')
  });
}

  applyFilters(): void {
    const text = this.searchTeam.trim().toLowerCase();

    this.filteredMatches = this.matches.filter(m => {
      const stadium = this.stadiumById[m.stadiumId];

      const matchStatus =
        this.selectedStatus === 'ALL' ||
        m.status === this.selectedStatus;

      // filtre phase
      const matchPhase =
        this.selectedPhase === 'ALL' ||
        m.phase === this.selectedPhase;

      // filtre pays
      const matchCountry =
        this.selectedCountry === 'ALL' ||
        (stadium && stadium.country === this.selectedCountry);

      // filtre stade
      const matchStadium =
        this.selectedStadium === 'ALL' ||
        (stadium && stadium.name === this.selectedStadium);

      // filtre équipe (home/away)
      const matchTeam =
        text.length === 0 ||
        m.homeTeam.toLowerCase().includes(text) ||
        m.awayTeam.toLowerCase().includes(text);

      return matchStatus && matchPhase && matchCountry && matchStadium && matchTeam;
    });
  }

  statusLabel(status: string): string {
    switch (status) {
      case 'SCHEDULED': return 'À venir';
      case 'LIVE': return 'En cours';
      case 'FINISHED': return 'Terminé';
      default: return status;
    }
  }

  phaseLabel(phase?: MatchPhase): string {
    switch (phase) {
      case 'GROUP': return 'Phase de groupes';
      case 'ROUND_OF_16': return 'Huitièmes de finale';
      case 'QUARTER_FINAL': return 'Quarts de finale';
      case 'SEMI_FINAL': return 'Demi-finale';
      case 'THIRD_PLACE': return 'Match pour la 3ᵉ place';
      case 'FINAL': return 'Finale';
      default: return 'Phase inconnue';
    }
  }
 

}
