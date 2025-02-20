package com.documentManagementSystem.server.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.documentManagementSystem.server.entity.Document;
import com.documentManagementSystem.server.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
	@Autowired
	private DocumentService documentService;

	// Endpoint to upload a document
	@PostMapping("/upload/{folderId}")
	public ResponseEntity<String> uploadDocument(
			@RequestParam("file") MultipartFile file,
			/* @RequestParam("folderId") */
			@PathVariable Long folderId,
			Authentication authentication
			) {
		try {
			// Get the authenticated user's email/username

			documentService.uploadDocument(file, folderId,authentication.getName());
			return ResponseEntity.ok("Document uploaded successfully");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Error uploading document: " + e.getMessage());
		}
	}

	// Endpoint to fetch a document by ID
	@GetMapping("/{id}")
	public ResponseEntity<Document> getDocumentById(@PathVariable Long id,Authentication authentication) {
		Document document = documentService.getDocumentById(id,authentication.getName());
		return ResponseEntity.ok(document);
	}

	// Endpoint to fetch all documents in a folder
	@GetMapping("/folder/{folderId}")
	public ResponseEntity<List<Document>> getDocumentsByFolder(@PathVariable Long folderId,Authentication authentication) {
		List<Document> documents = documentService.getDocumentsByFolder(folderId,authentication.getName());
		return ResponseEntity.ok(documents);
	}
	
	@GetMapping
	public ResponseEntity<List<Document>> getAllDocumentsByUser(Authentication authentication){
		return ResponseEntity.ok(documentService.getAllDocumentsByUser(authentication.getName()));
	}

	// Endpoint to delete a document
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDocument(@PathVariable Long id,Authentication authentication) {
		documentService.deleteDocument(id,authentication.getName());
		return ResponseEntity.ok("Document deleted successfully");
	}
}
