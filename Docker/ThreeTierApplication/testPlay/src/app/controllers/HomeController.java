package controllers;

import play.mvc.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool; 
import redis.clients.jedis.JedisPoolConfig; 
import java.net.InetAddress;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

import com.mongodb.ServerAddress;
import java.util.Arrays;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

     int count = 0;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() throws Exception{
           DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	   Date date = new Date();

        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool pool = new JedisPool(config, "redis", 6379);
        Jedis jedis = pool.getResource();
        InetAddress addr = InetAddress.getLocalHost();
        MongoClient mongoClient = new MongoClient( "mongo" , 27017 );
               DB db = mongoClient.getDB( "DockerTest" );
              // DBCollection coll = db.createCollection("Test");
               DBCollection coll = db.getCollection("Test");
               DBCursor cursor = coll.find().sort(new BasicDBObject("_id",-1)).limit(1);
               int x=0;
                if(cursor.hasNext()){
               jedis.set("count",cursor.next().get("Count").toString());
               x = Integer.parseInt(jedis.get("count"));}
               else{
                   x = 0;
		   jedis.set("count","0");
               }
               BasicDBObject doc = new BasicDBObject("Count",++x);


               doc.put("IPAddress", addr.getHostAddress());
               doc.put("Date",dateFormat.format(date));
               coll.insert(doc);
               return ok(jedis.get("count"));
    }
    

    public Result getData(){
      MongoClient mongoClient = new MongoClient( "mongo" , 27017 );
               DB db = mongoClient.getDB( "DockerTest" );
              // DBCollection coll = db.createCollection("Test");
               DBCollection coll = db.getCollection("Test");
               DBCursor cursor = coll.find();
         int i = 1;
	 String s = "";		
         while (cursor.hasNext()) { 
             s = s+ cursor.next().toString() + "\n";
            i++;
         }

       return ok(s);
}

}
