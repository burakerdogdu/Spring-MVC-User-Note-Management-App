package com.works.controllers;

import com.works.props.Notes;
import com.works.services.NotesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.jws.WebParam;

@Controller
public class NotesController {
    NotesService service = new NotesService();
    @GetMapping("/notes")
    public String notes(Model model){
        model.addAttribute("notes",service.notes());
        return "notes";
    }

    @PostMapping("/notesSave")
    public String notesSave(Notes notes,Model model){


        int status=service.notesSave(notes);
        model.addAttribute("notes",service.notes());

        return "notes";
    }
    @GetMapping("/notesDelete/{nid}")
    public String userDelete(@PathVariable int nid, Model model) {
        int status = service.deleteNote(nid);
        model.addAttribute("notes",service.notes());

        return "notes";
    }
}
