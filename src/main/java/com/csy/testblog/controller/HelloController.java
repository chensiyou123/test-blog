package com.csy.testblog.controller;

import com.csy.testblog.bean.Author;
import com.csy.testblog.bean.Book;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(Model model) {
        Map<String, Object> map = model.asMap();
        System.out.println(map);
        int i = 1 / 0;
        return "hello controller advice";
    }


    @PostMapping("/book1")
    public void addBook(Book book, Author author) {
        System.out.println(book);
        System.out.println(author);
    }

    @PostMapping("/book")
    public void addBook1(@ModelAttribute("b") Book book, @ModelAttribute("a") Author author) {
        System.out.println(book);
        System.out.println(author);
    }

}
