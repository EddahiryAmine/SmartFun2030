import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, NavigationEnd } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { StadiumService } from '../../services/stadium.service';
import { Stadium } from '../../models/stadium.model';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';


@Component({
  selector: 'app-stadiums',
  standalone: true,
  imports: [  
  CommonModule,
  FormsModule,
  ReactiveFormsModule,
  RouterLink],
  templateUrl: './stadiums.html',
  styleUrls: ['./stadiums.css']
})
export class StadiumListComponent implements OnInit, OnDestroy {

  stadiums: Stadium[] = [];
  filteredStadiums: Stadium[] = [];

  selectedCountry: string = 'ALL';
  searchText: string = '';
  errorMessage: string = '';
  loading: boolean = false;

  private routeSub?: Subscription;

  constructor(
    private stadiumService: StadiumService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadStadiums();

    // 👉 Recharger quand on revient sur /stadiums (retour navigateur, clic, etc.)
    this.routeSub = this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
      .subscribe(() => {
        // on est de nouveau sur cette page → on recharge
        this.loadStadiums();
      });
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  loadStadiums() {
    this.loading = true;
    this.stadiumService.getAllStadiums().subscribe({
      next: (data) => {
        this.stadiums = data;
        this.filteredStadiums = data;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erreur lors du chargement des stades.';
        this.loading = false;
      }
    });
  }

  get countries(): string[] {
    const set = new Set<string>();
    this.stadiums.forEach(s => set.add(s.country));
    return Array.from(set);
  }

  applyFilters() {
    this.filteredStadiums = this.stadiums.filter(s => {
      const matchCountry =
        this.selectedCountry === 'ALL' || s.country === this.selectedCountry;

      const matchText = this.searchText.trim().length === 0 ||
        s.name.toLowerCase().includes(this.searchText.toLowerCase()) ||
        s.city.toLowerCase().includes(this.searchText.toLowerCase());

      return matchCountry && matchText;
    });
  }
}
