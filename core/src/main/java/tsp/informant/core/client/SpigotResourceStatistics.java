package tsp.informant.core.client;

public class SpigotResourceStatistics {

    private final int authorId;
    private final String authorUsername;
    private final double price;
    private final String currency;

    private final int downloads;
    private final int updates;
    private final int uniqueReviews;
    private final int totalReviews;
    private final double rating;

    public SpigotResourceStatistics(int authorId, String authorUsername, double price, String currency, int downloads, int updates, double rating, int uniqueReviews, int totalReviews) {
        this.authorId = authorId;
        this.authorUsername = authorUsername;

        this.price = price;
        this.currency = currency;

        this.downloads = downloads;
        this.updates = updates;
        this.uniqueReviews = uniqueReviews;
        this.totalReviews = totalReviews;
        this.rating = rating;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public int getDownloads() {
        return downloads;
    }

    public int getUpdates() {
        return updates;
    }

    public int getUniqueReviews() {
        return uniqueReviews;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public double getRating() {
        return rating;
    }

}