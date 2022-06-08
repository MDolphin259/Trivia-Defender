package com.example.ScoreRecordAPI.controller;

import com.example.ScoreRecordAPI.model.ScoreRecord;
import com.example.ScoreRecordAPI.model.ScoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")

public class ScoreRecordController {

    @Autowired
    private ScoreRecordService scoreService;

    @GetMapping("/getScores")
    public ArrayList<ScoreRecord> getScores() throws InterruptedException, ExecutionException {
        ArrayList<ScoreRecord> scoreList = scoreService.getScores();
        System.out.println("Posting records");
        return scoreList;
    }

    @PostMapping("/postScore")
    public String createScore(@RequestBody ScoreRecord s) throws InterruptedException, ExecutionException{
        System.out.println("Saving record");
        String response = scoreService.sendScore(s);
        return response;
    }

}
