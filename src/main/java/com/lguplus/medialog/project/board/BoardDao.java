package com.lguplus.medialog.project.board;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

// 매퍼 클래스
@Mapper
public interface BoardDao {
	public List<BoardVO> getList(Criteria criteria);
	
	public Integer selectBoardCount(PageVO pageVO);
	
	List<BoardVO> searchBoardListByTitle(String keyword);
	
	public List<BoardVO> searchBoardListByContent(String keyword);
	
    public List<?> selectBoardList(PageVO param);
    
    public List<?> selectBoardListByNew(PageVO param);
    
    public String boardSearchList();
	
	void uploadBoard(BoardVO board);
	
	public BoardVO selectBoardParent(String brdParent);
	
	public Integer selectBoardMaxOrder(Integer s);
	
	public void updateBoardOrder(BoardVO board);
	
	public void updateBoardOrderDelete(Integer id);
	
	
	void uploadFile(FileVO fileVO);
	
	BoardVO getBoardDetail(Integer id);
	
	public void boardModifyRegist(BoardVO board);
	
	public void boardDelete(Integer id);
	
	public int boardViewUpdate(Integer id);

	void commentPost(ReplyVO comment);
	
	List<ReplyVO> openCommentList(Integer id);
	
	public void addCommentCnt(String id);
	
	public void subCommentCnt(String id);
	
	public void commentDelete(String id);
	
	public Integer selectBoard6ReplyMaxOrder(String s);
	
	public void insertBoard6Reply(ReplyVO param);
	
	public void updateBoard6Reply(ReplyVO param);
	
	public ReplyVO selectBoard6ReplyParent(String commentParent);

	public void updateBoard6ReplyOrder(ReplyVO comment);
	
	public Integer selectBoard6ReplyChild(String param);
	
	public Integer selectBoardChild(Integer id);
	
	public void updateBoard6ReplyOrder4Delete(String param);
	
	public boolean deleteBoard6Reply(String param);
	
	public List<?> selectBoard6FileList(Integer id);
	
	public FileVO getFileList(Integer id);
	
    public void deleteFile(String id);
    
    public void deleteFileByParents(Integer id);
    
    public void commentDeleteByParents(Integer id);
}
