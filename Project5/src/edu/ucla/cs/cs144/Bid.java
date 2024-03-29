package edu.ucla.cs.cs144;

public class Bid {
   private User bidder;
   private String time;
   private String amount;
   
   public Bid(final User bidder, final String time, final String amount) {
      this.bidder = bidder;
      this.time = time;
      this.amount = amount;
   }
   
   public User getBidder() {
      return this.bidder;
   }
   
   public String getTime() {
      return this.time;
   }
   
   public String getAmount() {
      return this.amount;
   }
}