//package com.paw.controllers;
//
//import com.paw.business.interfaces.I_GameTableService;
//import com.paw.business.interfaces.I_UsersService;
//import com.paw.persistence.entities.Users;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.*;
//import java.util.concurrent.CopyOnWriteArrayList;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.context.event.EventListener;
//
//@Controller
//public class SSEController {
//    private final CopyOnWriteArrayList<SseEmitter> playersEmitters = new CopyOnWriteArrayList<>();
//    private final CopyOnWriteArrayList<SseEmitter> readyPlayersEmitters = new CopyOnWriteArrayList<>();
//    private static boolean maxUsers = false;
//    private static boolean allReady = false;
//    private static int usersListLen = 0;
//
//    @Autowired
//    I_UsersService usersService;
//
//    @Autowired
//    I_GameTableService gameTableService;
//
//
//    @GetMapping("/api/users/lobby/getUsers")
//    public SseEmitter getUsers(HttpServletResponse response, @RequestParam String table_id) {
//        response.setHeader("Cache-Control", "no-store");
//        SseEmitter emitter = new SseEmitter(-1L);
//        maxUsers = false;
//
//        this.playersEmitters.add(emitter);
//
//        emitter.onCompletion(() -> this.playersEmitters.remove(emitter));
//        emitter.onTimeout(() -> this.playersEmitters.remove(emitter));
//        new Thread(()-> {
//            while(true)
//            {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                sendUsers(table_id);
//            }
//        }).start();
//
//        if(maxUsers)
//        {
//            emitter.complete();
//        }
//
//        return emitter;
//    }
//
//    @EventListener
//    public void sendUsers(String table_id) {
//        List<SseEmitter> deadEmitters = new ArrayList<>();
//        this.playersEmitters.forEach(emitter -> {
//            try {
//                List<Users> usersList = usersService.getAllUsers(table_id);
//                emitter.send(usersList);
//                usersListLen = usersList.size();
//
//                if (usersListLen == 6)
//                    maxUsers = true;
//            }
//            catch (Exception e) {
//                deadEmitters.add(emitter);
//            }
//        });
//
//        this.playersEmitters.removeAll(deadEmitters);
//    }
//
//    @GetMapping("/api/users/lobby/getReadyPlayers")
//    public SseEmitter getReadyPlayers(HttpServletResponse response, @RequestParam String table_id) {
//        response.setHeader("Cache-Control", "no-store");
//        SseEmitter emitter = new SseEmitter(-1L);
//        allReady = false;
//
//        this.readyPlayersEmitters.add(emitter);
//
//        emitter.onCompletion(() -> this.readyPlayersEmitters.remove(emitter));
//        emitter.onTimeout(() -> this.readyPlayersEmitters.remove(emitter));
//        // Mocking events with non stoppping thread
//        new Thread(()-> {
//            while(true)
//            {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                readyPlayers(table_id);
//            }
//        }).start();
//
//        if(allReady)
//        {
//            emitter.complete();
//        }
//
//        return emitter;
//    }
//
//    @EventListener
//    public void readyPlayers(String table_id) {
//        List<SseEmitter> deadEmitters = new ArrayList<>();
//        this.readyPlayersEmitters.forEach(readyEmitter -> {
//            try {
//                int playersReady = gameTableService.getPlayersReady(table_id);
//                if(playersReady == usersListLen)
//                    allReady = true;
//                readyEmitter.send(playersReady);
//            }
//            catch (Exception e) {
//                deadEmitters.add(readyEmitter);
//            }
//        });
//
//        this.readyPlayersEmitters.removeAll(deadEmitters);
//    }
//
//}
