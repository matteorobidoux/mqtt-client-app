/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datacomproject.mqttclientapp.mqtt;

import datacomproject.mqttclientapp.mqtt.KeyStore.KeyStoreHelper;

/**
 *
 * @author Matteo
 */
public class App {
    
    public static void main(String[]args) throws Exception{
        KeyStoreHelper ksh = new KeyStoreHelper();
        ksh.getUserInput();
    }
}
