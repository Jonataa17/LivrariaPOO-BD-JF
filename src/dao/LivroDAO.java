/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Livro;
import services.EditoraServicos;
import services.ServicosFactory;

/**
 *
 * @author 182120038
 */
public class LivroDAO {

    public void cadastrarLivroDAO(Livro liVO) {
        //busca conexão com o BD
        try {
            Connection con = Conexao.getConexao();
            //cria espaço de trabalho SQL, é a area no Java onde vamos executar os Scripts SQL
            String sql;

            sql = "insert into livros values"
                    + "(null, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, liVO.getTitulo());
            pst.setString(2, liVO.getAutor());
            pst.setString(3, liVO.getAssunto());
            pst.setString(4, liVO.getIsbn());
            pst.setInt(5, liVO.getEstoque());
            pst.setFloat(6, liVO.getPreco());
            pst.setInt(7, liVO.getIdEditora().getIdEditora());
            pst.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Erro ao cadastrar!\n"
                    + ex.getMessage());
        }
    }//fim cadastrarLivroDAO

    public ArrayList<Livro> getLivroDAO() {
        ArrayList<Livro> livros = new ArrayList<>();
        try {
            Connection con = Conexao.getConexao();
            Statement stat = con.createStatement();
            String sql = "select livros.*,e. cnpj from livros join editoras e using(ideditora)";
            ResultSet rs = stat.executeQuery(sql);

            EditoraServicos editoraS = ServicosFactory.getEditoraServicos();
            while (rs.next()) {
                Livro li = new Livro();
                //lado do java |x| (lado do banco)
                li.setIdLivro(rs.getInt("idlivro"));
                li.setTitulo(rs.getString("titulo"));
                li.setAutor(rs.getString("autor"));
                li.setAssunto(rs.getString("assunto"));
                li.setIsbn(rs.getString("isbn"));
                li.setEstoque(rs.getInt("estoque"));
                li.setPreco(rs.getFloat("preco"));
                li.setIdEditora(editoraS.buscarEditoraByCNPJ(rs.getString("cnpj")));

                livros.add(li);
            }

        } catch (SQLException ex) {
            System.out.println("Erro ao Listar!\n"
                    + ex.getMessage());
        }
        return livros;
    }//fim do listar (ArrayList)

    public Livro getLivroByISBN(String isbn) {

        Livro li = new Livro();
        EditoraServicos editoraS = ServicosFactory.getEditoraServicos();

        try {
            Connection con = Conexao.getConexao();
           String sql = "select livros.*,e.cnpj from livros join editoras e using(ideditora) where isbn = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, isbn);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                //lado do java |x| (lado do banco)
                li.setIdLivro(rs.getInt("idlivro"));
                li.setTitulo(rs.getString("titulo"));
                li.setAutor(rs.getString("autor"));
                li.setAssunto(rs.getString("assunto"));
                li.setIsbn(rs.getString("isbn"));
                li.setEstoque(rs.getInt("estoque"));
                li.setPreco(rs.getFloat("preco"));
                li.setIdEditora(editoraS.buscarEditoraByCNPJ(rs.getString("cnpj")));
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao Consultar ISBN!\n"
                    + ex.getMessage());
        }
        return li;
    }// fim getLivroByDoc

    public void deletarLivroDAO(String isbn) {

         try {
            Connection con = Conexao.getConexao();
            //cria espaço de trabalho SQL, é a area no Java onde vamos executar os Scripts SQL
            String sql;

            sql = "delete from livros where isbn = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, isbn);
            pst.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Erro ao deletar livro!\n"
                    + ex.getMessage());
        }
    }//fim deletarLivroDAO

    public void atualizaLivroDAO(Livro liVO) {

        try {
            Connection con = Conexao.getConexao();
            //cria espaço de trabalho SQL, é a area no Java onde vamos executar os Scripts SQL
            String sql;

            sql = "update livros set estoque = ?, preco = ? where isbn = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, liVO.getEstoque());
            pst.setFloat(2, liVO.getPreco());
            pst.setString(3, liVO.getIsbn());
            pst.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar livro!\n"
                    + ex.getMessage());
        }

    }//fim atualizaEditoraByDoc

}
