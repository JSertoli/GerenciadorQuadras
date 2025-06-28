/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.projects.gerenciadorquadras.model;

import javax.persistence.Entity;

/**
 *
 * @author Joao Sertoli
 */
@Entity
public class Administrador extends Usuario {
    private String senha;

    // Getters e Setters
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}