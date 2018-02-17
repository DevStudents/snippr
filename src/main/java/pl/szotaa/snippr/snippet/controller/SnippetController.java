package pl.szotaa.snippr.snippet.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.service.SnippetService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping("/snippet")
public class SnippetController {

    private final SnippetService snippetService;

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> create(@RequestBody @Valid Snippet snippet, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        snippetService.save(snippet);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(snippet.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Snippet> getById(@PathVariable Long id){
        Snippet result = snippetService.getById(id);
        if(result == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isSnippetOwner(#id, authentication)")
    public ResponseEntity<Void> updateExisting(@PathVariable Long id, @RequestBody @Valid Snippet snippet, BindingResult bindingResult){
        if(!snippetService.exists(id)){
            return ResponseEntity.notFound().build();
        }

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        snippet.setId(id);
        snippetService.update(snippet);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isSnippetOwner(#id, authentication)")
    public ResponseEntity<Void> deleteExisting(@PathVariable Long id){
        if(!snippetService.exists(id)){
            return ResponseEntity.notFound().build();
        }
        snippetService.delete(id);
        return ResponseEntity.ok().build();
    }
}
