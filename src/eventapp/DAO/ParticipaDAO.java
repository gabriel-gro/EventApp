package eventapp.DAO;

import eventapp.excecoes.sqlExcecao;
import eventapp.models.Avaliacao;
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;
import eventapp.models.Participa;
import eventapp.models.Usuario;
import eventapp.models.Evento;
import eventapp.util.Conn;
import java.io.IOException;
import java.util.ArrayList;



public class ParticipaDAO {
    
//    Insere uma nova participação no banco de dados
    public void insere(Participa participa) throws SQLException, ClassNotFoundException, sqlExcecao, IOException{
        String sql = "INSERT into participa VALUES(?,?,?,?)";
        PreparedStatement ps;
        ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1,participa.getIdUsuario());
        ps.setInt(2,participa.getIdEvento());
        ps.setInt(3,participa.getAvaliacao());
        ps.setString(4,participa.getComentario());
        ps.executeUpdate();
        Conn.fecharConexao();
    }
    
    // Insere uma nova nota no banco de dados
    public void insereAvaliacao(int idUsuario, int idEvento, int avaliacao) throws SQLException, ClassNotFoundException, sqlExcecao, IOException{
        String sql = "UPDATE participa SET avaliacao = ? WHERE id_usuario = ? and id_evento = ?";
        PreparedStatement ps;
        ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1,avaliacao);
        ps.setInt(2,idUsuario);
        ps.setInt(3,idEvento);
        ps.executeUpdate();
        Conn.fecharConexao();
    }
    
    // Insere um novo comentário no banco de dados
    public void insereComentario(int idUsuario, int idEvento, String comentario) throws SQLException, ClassNotFoundException, sqlExcecao, IOException{
        String sql = "UPDATE participa SET comentario = ? WHERE id_usuario = ? and id_evento = ?";
        PreparedStatement ps;
        ps = Conn.conectar().prepareStatement(sql);
        ps.setString(1,comentario);
        ps.setInt(2,idUsuario);
        ps.setInt(3,idEvento);
        ps.executeUpdate();
        Conn.fecharConexao();
    }
    
    public void insereComentarioEAvaliacao(int idUsuario, int idEvento, String comentario, int avaliacao) throws SQLException, ClassNotFoundException, sqlExcecao, IOException{
        String sql = "UPDATE participa SET comentario = ?, avaliacao = ? WHERE id_usuario = ? and id_evento = ?";
        PreparedStatement ps;
        ps = Conn.conectar().prepareStatement(sql);
        ps.setString(1,comentario);
        ps.setInt(2, avaliacao);
        ps.setInt(3,idUsuario);
        ps.setInt(4,idEvento);
        ps.executeUpdate();
        Conn.fecharConexao();
    }
    
    // Retorna a avaliação média do evento
    public double notaMedia(int idEvento) throws SQLException, ClassNotFoundException, sqlExcecao, IOException{
        String sql = "SELECT AVG(avaliacao)from participa where id_evento = ?";
        PreparedStatement ps;
        ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1,idEvento);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        rs.next();
        return (rs.getDouble("AVG(avaliacao)"));
    }
    
    // Retorna uma lista de usuários que está cadastrado no evento
    public ArrayList<Usuario> listaCadastrados(int idEvento) throws SQLException, ClassNotFoundException, sqlExcecao, IOException{
        String sql = "SELECT usuario.nome, usuario.usuario, usuario.email FROM usuario INNER JOIN participa ON usuario.id = participa.id_usuario where participa.id_evento = ?";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1, idEvento);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Usuario> usuarios = new ArrayList();
        while (rs.next()) {
            Usuario u = new Usuario(rs.getString("nome"), rs.getString("usuario"), rs.getString("email"), "---");
            usuarios.add(u);
        }
        return usuarios;
    }
    
    // Retorna a lista de eventos que o usuário está participando
    public ArrayList<Evento> listaEventosParticipante(int idUsuario) throws Exception, sqlExcecao{
        String sql = "SELECT evento.nome, evento.descricao, evento.data_inicio, evento.data_fim, evento.id_criador, evento.local_evento FROM evento INNER JOIN participa ON evento.id = participa.id_evento where participa.id_usuario = ?";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1, idUsuario);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Evento> eventos = new ArrayList();
        while (rs.next()) {
            Evento e = new Evento(rs.getString("nome"), rs.getString("descricao"), rs.getDate("data_inicio"), rs.getDate("data_fim"), rs.getInt("id_criador") ,rs.getString("local_evento"));
            eventos.add(e);
        }
        return eventos;
    }   
    
    // excluindo participação do usuario
    public void deletar(int idUsuario, int idEvento) throws sqlExcecao, SQLException, ClassNotFoundException, IOException{
        String sql = "DELETE FROM participa WHERE id_usuario = ? and id_evento = ?";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1, idUsuario);
        ps.setInt(2, idEvento);
        if (ps.executeUpdate() == 0) {
             throw new sqlExcecao("Não foi possivel deletar participação");
        }
        Conn.fecharConexao();
    }   

    public void deletar(int idEvento) throws Exception, sqlExcecao{
        String sql = "DELETE FROM participa WHERE id_evento = ?";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1, idEvento);
        if(ps.executeUpdate() != 0) {
            throw new sqlExcecao("Não foi possivel deletar participação");
        }
        Conn.fecharConexao();
    }  
    
    //retorna lista dos comentários do evento e avaliações dos usuários
    public ArrayList<Avaliacao> todosComentarios(int idEvento) throws Exception, sqlExcecao{
        String sql = "SELECT idUsuario, nome, avaliacao, comentario"
                + " FROM participa JOIN usuario on idUsuario = id "
                + "WHERE id_evento = ?";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
         ps.setInt(1, idEvento);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Avaliacao> avaliacoes = new ArrayList();
        while (rs.next()) {
            Avaliacao a = new Avaliacao(rs.getInt("idUsuario"), rs.getString("nome"), rs.getInt("nota"), rs.getString("comentario"));
            avaliacoes.add(a);
        }
        return avaliacoes;
    }  
    
    //retorna os dados do evento que o usuário está participando

}
