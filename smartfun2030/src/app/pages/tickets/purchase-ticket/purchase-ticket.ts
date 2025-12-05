import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TicketService, PurchaseTicketRequest } from '../../../services/ticket.service';
import { AuthService } from '../../../services/auth.service';
@Component({
  selector: 'app-purchase-ticket',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase-ticket.html',
  styleUrls: ['./purchase-ticket.css']
})
export class PurchaseTicketComponent implements OnInit {

  matchId!: string;

  availability: number = 0;
  loading: boolean = true;
  errorMessage: string = '';
  successMessage: string = '';

  category: 'NORMAL' | 'VIP' | 'VVIP' = 'NORMAL';
  quantity: number = 1;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ticketService: TicketService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.matchId = this.route.snapshot.paramMap.get('matchId')!;

    // sécurité : si non connecté, on renvoie vers login
    if (!this.auth.isLoggedIn || !this.auth.isLoggedIn()) {
      this.router.navigate(['/login'], {
        queryParams: { redirect: `/tickets/acheter/${this.matchId}` }
      });
      return;
    }

    this.loadAvailability();
  }

  loadAvailability(): void {
    this.loading = true;
    this.ticketService.getAvailability(this.matchId).subscribe({
      next: (value) => {
        this.availability = value;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors du chargement des places disponibles.";
        this.loading = false;
      }
    });
  }

  onPurchase(): void {
  this.errorMessage = '';
  this.successMessage = '';

  if (this.quantity <= 0) {
    this.errorMessage = 'La quantité doit être au moins 1.';
    return;
  }

  if (this.quantity > this.availability) {
    this.errorMessage = "Pas assez de places disponibles.";
    return;
  }

  const userId = this.auth.getUserId();
  if (!userId) {
    this.errorMessage = "Utilisateur non identifié (token invalide ?)";
    return;
  }

  const payload: PurchaseTicketRequest = {
    matchId: this.matchId,
    category: this.category,
    quantity: this.quantity
  };

  this.ticketService.purchase(payload, userId).subscribe({
    next: (ticket) => {
      this.successMessage = 'Ticket acheté avec succès !';
      this.loadAvailability();
    },
    error: (err) => {
      console.error(err);
      this.errorMessage = err.error?.message || "Erreur lors de l'achat du ticket.";
    }
  });
}

}
