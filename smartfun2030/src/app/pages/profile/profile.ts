import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';
import { TicketService, TicketResponse, Favorite, CartItem } from '../../services/ticket.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css']
})
export class ProfileComponent implements OnInit {

  email: string | null = null;
  roles: string[] = [];

  // TICKETS
  myTickets: TicketResponse[] = [];
  loadingTickets = false;

  // FAVORIS
  favorites: Favorite[] = [];
  loadingFavorites = false;

  // PANIER
  cart: CartItem[] = [];
  loadingCart = false;

  ticketError = '';
  favoriteError = '';
  cartError = '';

  constructor(
    private auth: AuthService,
    private router: Router,
    private ticketService: TicketService
  ) {}

  ngOnInit(): void {

    // 🔒 Sécurité
    if (!this.auth.isLoggedIn()) {
      this.router.navigate(['/login'], {
        queryParams: { redirect: '/profile' }
      });
      return;
    }

    this.email = this.auth.getEmail();
    this.roles = this.auth.getRoles();

    this.loadMyTickets();
    this.loadFavorites();
    this.loadCart();
  }

  get userId(): string | null {
    return this.auth.getUserId();
  }

  // ------------------ TICKETS ------------------
  private loadMyTickets(): void {
    this.loadingTickets = true;

    this.ticketService.getMyTickets(this.userId!).subscribe({
      next: (res) => {
        this.myTickets = res;
        this.loadingTickets = false;
      },
      error: () => {
        this.ticketError = "Erreur lors du chargement de vos tickets.";
        this.loadingTickets = false;
      }
    });
  }

  // ------------------ FAVORIS ------------------
  loadFavorites(): void {
    this.loadingFavorites = true;

    this.ticketService.getFavorites(this.userId!).subscribe({
      next: (res) => {
        this.favorites = res;
        this.loadingFavorites = false;
      },
      error: () => {
        this.favoriteError = "Erreur chargement favoris";
        this.loadingFavorites = false;
      }
    });
  }

  removeFavorite(matchId: string): void {
    this.ticketService.removeFavorite(matchId, this.userId!).subscribe({
      next: () => this.loadFavorites(),
      error: () => alert("Erreur suppression favori")
    });
  }

  // ------------------ PANIER ------------------
  loadCart(): void {
    this.loadingCart = true;

    this.ticketService.getCart(this.userId!).subscribe({
      next: (res) => {
        this.cart = res;
        this.loadingCart = false;
      },
      error: () => {
        this.cartError = "Erreur chargement du panier";
        this.loadingCart = false;
      }
    });
  }

  removeCartItem(itemId: string): void {
    this.ticketService.removeCartItem(itemId, this.userId!).subscribe({
      next: () => this.loadCart(),
      error: () => alert("Erreur suppression panier")
    });
  }

  clearCart(): void {
    this.ticketService.clearCart(this.userId!).subscribe({
      next: () => this.loadCart(),
      error: () => alert("Erreur vider panier")
    });
  }

  getCartTotal(): number {
    return this.cart.reduce((sum, item) => sum + (item.unitPrice * item.quantity), 0);
  }
}
