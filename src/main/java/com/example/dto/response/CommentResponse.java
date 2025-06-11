package com.example.dto.response;

public record CommentResponse(
       Long id,
       String content,
       String fecha,
       Long documentId,
       Integer userId

) {
}
//public class Comment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String contenido;
//    private LocalDateTime fecha;
//
//    @ManyToOne
//  @JsonBackReference
//    private Document document;
//
//    @ManyToOne
//    @JoinColumn(name = "usuario_id", nullable = false)
//    private User usuario;