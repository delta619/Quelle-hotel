package VeloGenerator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VeloGen {
    // velocity engine for rendering home.java


    public static void main( String[] args )
            throws Exception
    {
        /*
         *   first, get and initialize an engine
         */

        VelocityEngine ve = new VelocityEngine();
        ve.init();

        /*
         *   organize our data
         */

        ArrayList list = new ArrayList();
        Map map = new HashMap();

        map.put("name", "Cow");
        map.put("price", "$100.00");
        list.add( map );

        map = new HashMap();
        map.put("name", "Eagle");
        map.put("price", "$59.99");
        list.add( map );

        map = new HashMap();
        map.put("name", "Shark");
        map.put("price", "$3.99");
        list.add( map );

        /*
         *  add that list to a VelocityContext
         */

        VelocityContext context = new VelocityContext();
        context.put("petList", list);

        /*
         *   get the Template
         */

        Template t = ve.getTemplate( "./src/main/java/templates/Home.html" );

        /*
         *  now render the template into a Writer, here
         *  a StringWriter
         */

        StringWriter writer = new StringWriter();

        t.merge( context, writer );

        /*
         *  use the output in the body of your emails
         */

//        System.out.println( writer.toString() );
    }
}
