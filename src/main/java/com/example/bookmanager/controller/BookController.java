package com.example.bookmanager.controller;

import com.example.bookmanager.model.Book;
import com.example.bookmanager.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository repository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String list(Model model) {
        List<Book> books = repository.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showForm(Model model) {
        model.addAttribute("book", new Book());
        return "form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@ModelAttribute Book book, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            if (book != null) {
                // Process image file if provided
                if (imageFile != null && !imageFile.isEmpty()) {
                    String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
                    String dataUrl = "data:" + imageFile.getContentType() + ";base64," + base64Image;
                    book.setImageData(dataUrl);
                    System.out.println("Image saved: " + dataUrl.substring(0, 50) + "...");
                } else if (book.getId() != null) {
                    // Keep existing image if no new file uploaded (when editing)
                    Optional<Book> existing = repository.findById(book.getId());
                    if (existing.isPresent()) {
                        book.setImageData(existing.get().getImageData());
                    }
                }
                repository.save(book);
                System.out.println("Book saved: " + book.getTitle());
            }
        } catch (Exception e) {
            System.err.println("Error saving book: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(@PathVariable Long id, Model model) {
        if (id != null) {
            Optional<Book> opt = repository.findById(id);
            if (opt.isPresent()) {
                model.addAttribute("book", opt.get());
                return "form";
            }
        }
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        if (id != null) {
            repository.deleteById(id);
        }
        return "redirect:/books";
    }
}