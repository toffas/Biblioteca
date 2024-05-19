import java.util.Date;

public class Main {
    public static void main(String[] args) {
        boolean exit = false;
        LeitorServico leitor = new LeitorServico();

        Repositorio<Usuario> usuarios = new Repositorio<>();
        Repositorio<Livro> estoque = new Repositorio<>();
        Repositorio<Emprestimo> emprestimos = new Repositorio<>();

        while (!exit) {
            int opcao = leitor.lerOpcao();

            Usuario usuario;
            Livro livro;

            switch (opcao) {
                case 1: // Emprestimo
                case 2: // Devolucao
                    usuario = usuarios.buscarPorFiltro(usr -> usr.getNome().equals(leitor.lerNomeUsuario()));
                    if (usuario == null) {
                        System.err.println("ERRO: Usuario nao encontrado!");
                        break;
                    }

                    livro = estoque.buscarPorFiltro(l -> l.getNome().equals(leitor.lerNomeLivro()));

                    if (livro == null || livro.getQuantidadeEmEstoque() <= 0) {
                        System.err.println("ERRO: Livro nao encontrado!");
                        break;
                    }

                    if (opcao == 1) {
                        usuario.addLivro(livro);
                        Emprestimo emprestimo = new Emprestimo(usuario, livro, new Date());
                        emprestimos.salvar(emprestimo);

                        System.out.println("Livro emprestado com sucesso!");
                        break;
                    }

                    usuario.removeLivro(livro);
                    Livro finalLivro = livro;
                    Emprestimo emprestimo = emprestimos.buscarPorFiltro(e -> e.getUsuario().equals(usuario) && e.getLivro().equals(finalLivro));
                    emprestimo.setDataDevolucao(new Date());

                    System.out.println("Livro devolvido com sucesso!");
                    break;
                case 3: // Buscar livros
                    int opcaoBuscar = leitor.lerOpcaoBuscarLivro();

                    if (opcaoBuscar == 1) {
                        livro = estoque.buscarPorFiltro(l -> l.getNome().equals(leitor.lerNomeLivro()));
                    } else if (opcaoBuscar == 2) {
                        livro = estoque.buscarPorFiltro(l -> l.getId().equals(leitor.lerID()));
                    } else if (opcaoBuscar == 3) {
                        livro = estoque.buscarPorFiltro(l -> l.getAutor().equals(leitor.lerAutor()));
                    } else {
                        System.err.println("ERRO: opcao invalida!");
                        break;
                    }

                    if (livro == null) {
                        System.err.println("ERRO: Livro nao encontrado!");
                        break;
                    }

                    System.out.println(livro);
                    break;
                case 4: // Gerenciar Usuarios
                    int opcaoUsuario = leitor.lerOpcaoGerenciar();

                    if (opcaoUsuario == 1) {
                        Usuario novoUsuario = new Usuario();
                        novoUsuario.setId(leitor.lerID());
                        novoUsuario.setNome(leitor.lerNomeUsuario());

                        if (usuarios.buscarPorFiltro(u -> u.getNome().equals(novoUsuario.getNome())) != null) {
                            System.err.println("ERRO: Ja existe um usuario com esse nome!");
                            break;
                        }

                        usuarios.salvar(novoUsuario);
                        break;
                    }

                    usuario = usuarios.buscarPorFiltro(usr -> usr.getNome().equals(leitor.lerNomeUsuario()));

                    if (usuario == null) {
                        System.err.println("ERRO: Usuario nao encontrado!");
                        break;
                    }

                    if (opcaoUsuario == 2) {
                        if (usuarios.removerPorFiltro(u -> u.getNome().equals(usuario.getNome()))) {
                            System.out.println("Usuario removido com sucesso!");
                            break;
                        }
                        System.err.println("ERRO: Usuario nao encontrado!");
                        break;
                    } else if (opcaoUsuario == 3) {
                        usuario.setNome(leitor.lerNomeUsuario());
                        System.out.println("Usuario alterado com sucesso!");
                        break;
                    }

                    System.err.println("ERRO: Opcao invalida!");
                    break;
                case 5:
                    int opcaoLivro = leitor.lerOpcaoGerenciar();
                    livro = estoque.buscarPorFiltro(l -> l.getNome().equals(leitor.lerNomeLivro()));

                    if (opcaoLivro == 1) {
                        if (livro != null) {
                            livro.setQuantidadeEmEstoque(livro.getQuantidadeEmEstoque() + 1);
                            break;
                        }
                        Livro novoLivro = new Livro();
                        novoLivro.setId(leitor.lerID());
                        novoLivro.setAutor(leitor.lerAutor());
                        novoLivro.setNome(leitor.lerNomeLivro());
                        novoLivro.setQuantidadeEmEstoque(1);
                        System.out.println("Livro adicionado com sucesso!");
                        estoque.salvar(novoLivro);

                        break;
                    }

                    if (livro == null) {
                        System.err.println("ERRO: Livro nao encontrado!");
                        break;
                    }

                    if (opcaoLivro == 2) {
                        if (estoque.removerPorFiltro(l -> l.getNome().equals(livro.getNome()))) {
                            System.out.println("Livro removido com sucesso!");
                            break;
                        }
                        System.err.println("ERRO: Livro nao encontrado!");
                        break;
                    } else if (opcaoLivro == 3) {
                        livro.setNome(leitor.lerNomeLivro());
                        livro.setId(leitor.lerID());
                        livro.setAutor(leitor.lerAutor());
                        livro.setQuantidadeEmEstoque(leitor.lerQuantidade());
                        System.out.println("Livro alterado com sucesso!");
                        break;
                    }
                    System.err.println("ERRO: Opcao invalida!");
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.err.println("ERRO: opcao invalida!");
                    break;
            }

        }
        leitor.close();
    }
}
