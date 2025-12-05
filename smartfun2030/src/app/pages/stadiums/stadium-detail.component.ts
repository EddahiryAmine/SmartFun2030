import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { StadiumService } from '../../services/stadium.service';
import { MatchService } from '../../services/match.service';
import { Stadium } from '../../models/stadium.model';
import * as L from 'leaflet';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-stadium-detail',
  standalone: true,
   imports: [
    
    CommonModule,
    RouterLink
  ],
  templateUrl: './stadium-detail.component.html',
  styleUrls: ['./stadium-detail.component.css']
})
export class StadiumDetailComponent implements OnInit, OnDestroy {

  stadium?: Stadium;
  matches: any[] = [];
  errorMessage: string = '';
  loading: boolean = false;

  private routeSub?: Subscription;
  private matchSub?: Subscription;
  private map?: L.Map; 
  constructor(
    private route: ActivatedRoute,
    private stadiumService: StadiumService,
    private matchService: MatchService
  ) {}

  ngOnInit(): void {
    console.log('[StadiumDetail] ngOnInit');

    this.routeSub = this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      console.log('[StadiumDetail] param id =', id);
      if (id) {
        this.loadData(id);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
    this.matchSub?.unsubscribe();
    this.map?.remove();
  }

  loadData(id: string): void {
    console.log('[StadiumDetail] loadData', id);
    this.loading = true;
    this.errorMessage = '';
    this.stadium = undefined;
    this.matches = [];

    this.stadiumService.getStadiumById(id).subscribe({
      next: (data) => {
        console.log('[StadiumDetail] stade reçu', data);
        this.stadium = data;
        this.loading = false;
        setTimeout(() => this.initMap(), 0);
      },
      error: (err) => {
        console.error('[StadiumDetail] ERREUR stade', err);
        this.errorMessage = "Erreur lors du chargement du stade.";
        this.loading = false;
      }
    });

    this.matchSub = this.matchService.getMatchesByStadium(id).subscribe({
      next: (data) => {
        console.log('[StadiumDetail] matchs reçus', data);
        this.matches = data;
      },
      error: (err) => {
        console.error("[StadiumDetail] ERREUR matchs", err);
      }
    });
  }

  googleMapsUrl(): string {
    if (!this.stadium) return '';
    return `https://www.google.com/maps?q=${this.stadium.latitude},${this.stadium.longitude}`;
  }
  private initMap(): void {
    if (!this.stadium) return;

    // Si une carte existe déjà (navigation avant/arrière), on la détruit
    if (this.map) {
      this.map.remove();
    }

    this.map = L.map('map').setView(
      [this.stadium.latitude, this.stadium.longitude],
      14
    );

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap'
    }).addTo(this.map);

    const stadiumIcon = L.icon({
      iconUrl: 'https://cdn-icons-png.flaticon.com/512/854/854866.png',
      iconSize: [40, 40],
      iconAnchor: [20, 40]
    });

    L.marker(
      [this.stadium.latitude, this.stadium.longitude],
      { icon: stadiumIcon }
    )
      .addTo(this.map)
      .bindPopup(`<b>${this.stadium.name}</b><br>${this.stadium.city}`);
  }
  openInGoogleMaps(): void {
  if (!this.stadium) return;
  const url = `https://www.google.com/maps?q=${this.stadium.latitude},${this.stadium.longitude}`;
  window.open(url, '_blank');
}

openItineraryFromUser(): void {
  if (!this.stadium) return;

  if (!navigator.geolocation) {
    alert("La géolocalisation n'est pas supportée par ce navigateur.");
    return;
  }

  navigator.geolocation.getCurrentPosition(
    (pos) => {
      const userLat = pos.coords.latitude;
      const userLng = pos.coords.longitude;

      if (!this.stadium) return;
      const url = `https://www.google.com/maps/dir/?api=1&origin=${userLat},${userLng}&destination=${this.stadium.latitude},${this.stadium.longitude}`;
      window.open(url, '_blank');
    },
    (err) => {
      console.error(err);
      alert("Impossible de récupérer votre position.");
    }
  );
}

}
