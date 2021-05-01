/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackproject;

import blackjackproject.Card.Rank;
import blackjackproject.Card.Suit;
/**
 *
 * @author acer
 */
public class Deck {

    private Card[] cards = new Card[52];
    
    public Deck() {
        refill();
    }
    
    
    public final void refill() {
        int i = 0;
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards[i++] = new Card(suit, rank);
            }
        }
    }
    
     public Card drawCard() {
        Card card = null;
        while (card == null) {
            int index = (int)(Math.random()*cards.length);
            card = cards[index];
            cards[index] = null;
        }
        return card;
    }
    
}
