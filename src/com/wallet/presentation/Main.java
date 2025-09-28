package com.wallet.presentation;


import java.sql.Connection;
import connexiondb.DbConnection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            if (conn != null) {
                System.out.println("Connexion réussie");
            } else {
                System.out.println("Connexion échouée ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

