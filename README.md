
# 🏀 Gerenciador de Quadras Poliesportivas

Projeto Java desenvolvido para disciplina de Programação Orientada a Objetos (POO), com o objetivo de aplicar os principais conceitos de:

- ✅ **Modelagem orientada a objetos**
- ✅ **Persistência com banco de dados usando JPA (Hibernate)**
- ✅ **Interface gráfica com Swing (Java Swing GUI)**

---

## 📌 Funcionalidades

- Cadastro, edição e remoção de **Jogadores** e **Quadras**
- Reserva de quadras por jogadores em uma determinada **data, hora e duração**
- Registro opcional de **pagamento** no momento da reserva
- Listagem e gerenciamento de reservas e quadras com **JTable**
- Campos de data e hora com componentes amigáveis (ex: `JDateChooser`)
- Atualização dinâmica dos dados entre telas

---

## 🛠 Tecnologias e Ferramentas

- Java 17+
- Swing (interface gráfica)
- JPA (Hibernate) para persistência
- PostgreSQL (banco de dados)
- `JDateChooser` para seleção de datas
- `JComboBox` com filtro e busca

---

## ⚙️ Como executar

1. Clone o repositório e abra o projeto em sua IDE (NetBeans, IntelliJ, Eclipse...)
2. Configure seu banco PostgreSQL e crie um banco chamado `gerenciador_quadras`
3. Atualize o arquivo `persistence.xml` com suas credenciais do banco
4. Execute a classe `Main` (caso já esteja configurada) para iniciar a interface

---

## 🧱 Estrutura principal

- `Usuario` (classe abstrata)
  - `Jogador` (extends Usuario)
  - `Administrador` (extends Usuario)
- `Quadra`
- `Reserva`
- `Pagamento`

---

## 📁 Telas disponíveis

- **Cadastro de Jogadores**
- **Cadastro de Quadras**
- **Reserva de Quadras**
- **Listagem de Quadras com Edição/Exclusão**

---

## ✍️ Autor

Projeto desenvolvido por **João Gabriel Sertoli** como trabalho final da disciplina de POO.
