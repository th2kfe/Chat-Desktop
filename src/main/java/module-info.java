module com.example.chatdesktop {

    requires javafx.controls;
    requires java.net.http;

    exports com.example.chatdesktop;
    exports com.example.chatdesktop.config;
    exports com.example.chatdesktop.controller;
    exports com.example.chatdesktop.model;
    exports com.example.chatdesktop.service;
    exports com.example.chatdesktop.view;
}