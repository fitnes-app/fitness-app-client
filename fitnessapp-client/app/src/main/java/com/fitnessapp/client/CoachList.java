package com.fitnessapp.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.os.Bundle;
import android.app.Activity;

import com.fitnessapp.client.Utils.StaticStrings;

public class CoachList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Desde la version 3 de android, no se permite abrir una conexión de red desde el thread principal.
        //Por lo tanto se debe crear uno nuevo.
        sqlThread.start();
    }


    Thread sqlThread = new Thread() {
        public void run() {
            try {
                Class.forName("org.postgresql.Driver");
                // Si estás utilizando el emulador de android y tenes el PostgreSQL en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
                Connection conn = DriverManager.getConnection(
                        "http://localhost:8080/fitness-app-api-web/api", "postgres", "asdf1234");
                //En el stsql se puede agregar cualquier consulta SQL deseada.
                String stsql = "Select version()";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(stsql);
                rs.next();
                System.out.println(rs.getString(1));
                conn.close();
            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }
        }
    };
}