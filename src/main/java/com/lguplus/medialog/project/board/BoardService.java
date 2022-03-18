package com.lguplus.medialog.project.board;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	@Autowired
	private BoardDao dao;
	private DataSourceTransactionManager txManager;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	long start = System.currentTimeMillis();

    public Integer selectBoardCount() throws Exception {
		return dao.selectBoardCount();
    }
	public List<BoardVO> searchBoardList(String keyword)  {
		return dao.searchBoardList(keyword);
	}
    public List<?> selectBoardList(PageVO param) throws Exception {
    	return dao.selectBoardList(param);
    }
    public String boardSearchList() {
    	return dao.boardSearchList();
    }
	public void uploadBoard(BoardVO board) {
	    dao.uploadBoard(board);
	}
	public void uploadFile(FileVO fileVO) {
		dao.uploadFile(fileVO);
	}
	
	
	public BoardVO getBoardDetail(int id) throws Exception{
		
		return dao.getBoardDetail(id);
	}
	
    public void boardModifyRegist(BoardVO board) throws Exception {
        dao.boardModifyRegist(board);
        
    }
    // 게시글 삭제
    public void boardDelete(int id) throws Exception {
        dao.boardDelete(id);
    }
	// 조회수 올리기
	public int boardViewUpdate(int id) throws Exception {
		return dao.boardViewUpdate(id);
	}
	
	 public void insertBoardReply(ReplyVO param) {
	        if (param.getReNo() == null || "".equals(param.getReNo())) {
	            if (param.getReParents() != null) {
	            	ReplyVO replyInfo = dao.selectBoard6ReplyParent(param.getReParents());
	                param.setReDepth(replyInfo.getReDepth());
	                param.setReOrder(replyInfo.getReOrder() + 1);
	                dao.updateBoard6ReplyOrder(replyInfo);
	            } else {
	            	
	                Integer reorder = dao.selectBoard6ReplyMaxOrder(param.getReBrdNo());
	                param.setReOrder(reorder);
	            }
	            dao.insertBoard6Reply(param);
	        } else {
	        	dao.updateBoard6Reply(param);
	        }
	    }   

	public List<ReplyVO> openCommentList(int id){
		return dao.openCommentList(id);
	}
	public void addCommentCnt(String id) {
		dao.addCommentCnt(id);
	}
	
	public void subCommentCnt(String id) {
		dao.subCommentCnt(id);
	}
	public void commentDelete(String id) {
		Integer cnt = dao.selectBoard6ReplyChild(id);
		
        if ( cnt > 0) {
            return ;
        }
		
		dao.commentDelete(id);
	}
	

    /**
     * 댓글 삭제.
     * 자식 댓글이 있으면 삭제 안됨. 
     */
    public boolean deleteBoard6Reply(String param) {
        Integer cnt = dao.selectBoard6ReplyChild(param);
        
        if ( cnt > 0) {
            return false;
        }
        
        dao.updateBoard6ReplyOrder4Delete(param);
        dao.deleteBoard6Reply(param);
        return true;
    } 

    public List<?> selectBoard6FileList(int id) {
    	return dao.selectBoard6FileList(id);
    }


	

}
