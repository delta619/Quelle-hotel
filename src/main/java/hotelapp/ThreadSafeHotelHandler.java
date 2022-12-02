package hotelapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This Class manages the methods of its parent class HotelHandler in a thread safe way.
 * */
public class ThreadSafeHotelHandler extends HotelHandler {


    private ReentrantReadWriteLock lock;
    public ThreadSafeHotelHandler() {
        super();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Hotel findHotelId(String hotelId){
        try{
            this.lock.readLock().lock();
            return super.findHotelId(hotelId);
        }
        finally {
            this.lock.readLock().unlock();
        }
    }
    @Override
    public JsonObject getHotelInfoJson(String hotelId){
        try{
            this.lock.readLock().lock();
            return super.getHotelInfoJson(hotelId);
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void insertHotels(Hotel[] hotels){
        try {
            this.lock.writeLock().lock();
            super.insertHotels(hotels);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public JsonArray findHotelsUsingSubstring(String hotelNameSubString) {
        try {
            this.lock.readLock().lock();
            return super.findHotelsUsingSubstring(hotelNameSubString);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public  ArrayList<Hotel> getAllHotels(){
        try {
            this.lock.readLock().lock();
            return super.getAllHotels();
        } finally {
            this.lock.readLock().unlock();
        }
    }
}
