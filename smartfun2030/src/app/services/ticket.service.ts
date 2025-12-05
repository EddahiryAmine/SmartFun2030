import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PurchaseTicketRequest {
  matchId: string;
  category: 'NORMAL' | 'VIP' | 'VVIP';
  quantity: number;
}

export interface TicketResponse {
  id: string;
  matchId: string;
  userId: string;
  category: 'NORMAL' | 'VIP' | 'VVIP';
  status: 'RESERVED' | 'PAID' | 'CANCELLED';
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  purchaseDate: string;
  qrCode: string;
}

/** ✅ Favoris */
export interface Favorite {
  id: string;
  userId: string;
  matchId: string;
  createdAt: string;
}

/** ✅ Panier */
export interface CartItem {
  id: string;
  userId: string;
  matchId: string;
  category: 'NORMAL' | 'VIP' | 'VVIP';
  quantity: number;
  unitPrice: number;
}

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private readonly API_URL = 'http://localhost:8080/api/tickets';

  constructor(private http: HttpClient) {}

  /** 🔹 Places disponibles pour un match */
  getAvailability(matchId: string): Observable<number> {
    return this.http.get<number>(`${this.API_URL}/availability/${matchId}`);
  }

  /** 🔹 Achat direct d’un ticket */
  purchase(request: PurchaseTicketRequest, userId: string): Observable<TicketResponse> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.post<TicketResponse>(
      `${this.API_URL}/purchase`,
      request,
      { headers }
    );
  }

  /** 🔹 Mes tickets */
  getMyTickets(userId: string): Observable<TicketResponse[]> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.get<TicketResponse[]>(
      `${this.API_URL}/my`,
      { headers }
    );
  }

  // -------------------------------------------------
  // ⭐ FAVORIS
  // -------------------------------------------------

  /** 🔹 Liste des matchs favoris du user */
  getFavorites(userId: string): Observable<Favorite[]> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.get<Favorite[]>(
      `${this.API_URL}/favorites`,
      { headers }
    );
  }

  /** 🔹 Ajouter un match aux favoris */
  addFavorite(matchId: string, userId: string): Observable<void> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.post<void>(
      `${this.API_URL}/favorites/${matchId}`,
      {},
      { headers }
    );
  }

  /** 🔹 Retirer un match des favoris */
  removeFavorite(matchId: string, userId: string): Observable<void> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.delete<void>(
      `${this.API_URL}/favorites/${matchId}`,
      { headers }
    );
  }

  // -------------------------------------------------
  // 🛒 PANIER
  // -------------------------------------------------

  /** 🔹 Récupérer le panier du user */
  getCart(userId: string): Observable<CartItem[]> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.get<CartItem[]>(
      `${this.API_URL}/cart`,
      { headers }
    );
  }

  /** 🔹 Ajouter une ligne dans le panier */
  addToCart(
    payload: { matchId: string; category: 'NORMAL' | 'VIP' | 'VVIP'; quantity: number },
    userId: string
  ): Observable<CartItem> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.post<CartItem>(
      `${this.API_URL}/cart`,
      payload,
      { headers }
    );
  }

  /** 🔹 Supprimer un item du panier */
  removeCartItem(itemId: string, userId: string): Observable<void> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.delete<void>(
      `${this.API_URL}/cart/${itemId}`,
      { headers }
    );
  }

  /** 🔹 Vider le panier */
  clearCart(userId: string): Observable<void> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.delete<void>(
      `${this.API_URL}/cart`,
      { headers }
    );
  }

  /** 🔹 Checkout du panier → création des tickets */
  checkout(userId: string): Observable<TicketResponse[]> {
    const headers = new HttpHeaders({
      'X-User-Id': userId
    });

    return this.http.post<TicketResponse[]>(
      `${this.API_URL}/cart/checkout`,
      {},
      { headers }
    );
  }
}
