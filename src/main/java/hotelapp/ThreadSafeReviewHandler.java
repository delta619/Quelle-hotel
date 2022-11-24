package hotelapp;

import com.google.gson.JsonArray;
import org.eclipse.jetty.server.PushBuilder;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This Class manages the methods of its parent class ReviewHandler in a thread safe way.
 * */
public class ThreadSafeReviewHandler extends ReviewHandler {

    ReentrantReadWriteLock lock;
    ThreadSafeReviewHandler(){
        super();
        lock = new ReentrantReadWriteLock();

    }

    @Override
    public void insertReviews(ArrayList<Review> reviews){
        try{
            lock.writeLock().lock();
            super.insertReviews(reviews);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean updateReview(String hotelId, String reviewId, String reviewTitle, String reviewText){
        try{
            lock.writeLock().lock();
            return super.updateReview(hotelId, reviewId, reviewTitle, reviewText);
        } finally {
            lock.writeLock().unlock();
        }
    }
    @Override
    public Review findReviewUsingHotelIdAndReviewId(String hotelId, String reviewId){
        try{
            lock.readLock().lock();
            return super.findReviewUsingHotelIdAndReviewId(hotelId, reviewId);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void insertReview(String title, String reviewText, String hotelId, String reviewId, String nickname, String date){
        try{
            lock.writeLock().lock();
            super.insertReview(title, reviewText, hotelId, reviewId, nickname, date);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setUpWords(){
        try{
            lock.writeLock().lock();
            super.setUpWords();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deleteReview(String hotelId, String reviewId){
        try{
            lock.writeLock().lock();
            return super.deleteReview(hotelId, reviewId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public TreeSet<Review> findReviewsByHotelId(String hotelId, Boolean printFormat){
        try{
            lock.readLock().lock();
            return super.findReviewsByHotelId(hotelId, printFormat);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public JsonArray findReviewsByHotelIdJson(String hotelId, int numReviews){
        try{
            lock.readLock().lock();
            return super.findReviewsByHotelIdJson(hotelId, numReviews);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ArrayList<ReviewWithFreq> findWords(String word){
        try{
            lock.readLock().lock();
            return super.findWords(word);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public JsonArray findWordsJson(String word, int numReviews){
        try{
            lock.readLock().lock();
            return super.findWordsJson(word, numReviews);
        } finally {
            lock.readLock().unlock();
        }
    }


}
