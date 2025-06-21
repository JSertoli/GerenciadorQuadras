/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.projects.courtsmanager.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author Joao Sertoli
 */
@Entity
public class Jogador extends Usuario {
    private String telefone;

    @Enumerated(EnumType.STRING)
    private NivelJogador nivel;

    // Getters e Setters
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public NivelJogador getNivel() { return nivel; }
    public void setNivel(NivelJogador nivel) { this.nivel = nivel; }
}
