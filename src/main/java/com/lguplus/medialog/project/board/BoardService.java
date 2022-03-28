package com.lguplus.medialog.project.board;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
public class BoardService {
	@Autowired
	private BoardDao dao;
	

    public Integer selectBoardCount(PageVO pageVO) throws Exception {
		return dao.selectBoardCount(pageVO);
    }
    
	public List<BoardVO> searchBoardListByTitle(String keyword)  {
		return dao.searchBoardListByTitle(keyword);
	}
	public List<BoardVO> searchBoardListByContent(String keyword)  {
		return dao.searchBoardListByContent(keyword);
	}
	public List<BoardVO> searchBoardListByOrigin()  {
		return dao.searchBoardListByOrigin();
	}
	public List<BoardVO> searchBoardListByChild()  {
		return dao.searchBoardListByChild();
	}
    public List<?> selectBoardList(PageVO param) throws Exception {
    	return dao.selectBoardList(param);
    }
    public List<?> selectBoardListByNew(PageVO param) throws Exception {
    	return dao.selectBoardListByNew(param);
    }
    public String boardSearchList() {
    	return dao.boardSearchList();
    }
	public void uploadBoard(BoardVO param) {
        if (param.getBrdNo() == null) {
            if (param.getBrdParents() != null) {
            	BoardVO brdInfo = dao.selectBoardParent(param.getBrdParents());
                param.setBrdDepth(brdInfo.getBrdDepth());
                param.setBrdOrder(brdInfo.getBrdOrder() + 1);
                dao.updateBoardOrder(brdInfo);
            } else {
                Integer brdorder = dao.selectBoardMaxOrder(param.getBrdNo());
                param.setBrdOrder(brdorder);
            }
            dao.uploadBoard(param);
        }
	}
  
	
	
	
	
	
	public void uploadFile(FileVO fileVO) {
		dao.uploadFile(fileVO);
	}

	public BoardVO getBoardDetail(Integer id){
		
		return dao.getBoardDetail(id);
	}
	
    public void boardModifyRegist(BoardVO board) throws Exception {
        dao.boardModifyRegist(board);
        
    }
    // 게시글 삭제
    public boolean boardDelete(Integer id) {
        Integer cnt = dao.selectBoardChild(id);
        
        if ( cnt > 0) {
            return false;
        }
        
        dao.updateBoardOrderDelete(id);
        dao.boardDelete(id);
        return true;
    } 
	// 조회수 올리기
	public int boardViewUpdate(Integer id){
		return dao.boardViewUpdate(id);
	}
	
	 public void insertBoardReply(ReplyVO param) {
	        if (param.getReNo() == null || "".equals(param.getReNo())) {
	            if (param.getReParents() != null) {
	            	ReplyVO replyInfo = dao.selectBoardReplyParent(param.getReParents());
	                param.setReDepth(replyInfo.getReDepth());
	                param.setReOrder(replyInfo.getReOrder() + 1);
	                dao.updateBoardReplyOrder(replyInfo);
	            } else {
	            	
	                Integer reorder = dao.selectBoardReplyMaxOrder(param.getReBrdNo());
	                param.setReOrder(reorder);
	            }
	            dao.insertBoardReply(param);
	        } else {
	        	dao.updateBoardReply(param);
	        }
	    }   

	public List<ReplyVO> openCommentList(Integer id){
		return dao.openCommentList(id);
	}
	public void addCommentCnt(String id) {
		dao.addCommentCnt(id);
	}
	
	public void subCommentCnt(String id) {
		dao.subCommentCnt(id);
	}
	public void commentDelete(String id) {
		Integer cnt = dao.selectBoardReplyChild(id);
		
        if ( cnt > 0) {
            return ;
        }
		
		dao.commentDelete(id);
	}
	

    /**
     * 댓글 삭제.
     * 자식 댓글이 있으면 삭제 안됨. 
     */
    public boolean deleteBoardReply(String param) {
        Integer cnt = dao.selectBoardReplyChild(param);
        
        if ( cnt > 0) {
            return false;
        }
        
        dao.updateBoardReplyOrder4Delete(param);
        dao.deleteBoardReply(param);
        return true;
    } 

    public List<?> selectBoardFileList(Integer id) {
    	return dao.selectBoardFileList(id);
    }
    public FileVO getFileList(Integer id) {
    	return dao.getFileList(id);
    }
    public void deleteFile(String id) {
    	dao.deleteFile(id);
    }
    public void deleteFileByParents(Integer id) {
    	dao.deleteFileByParents(id);
    }
    public void commentDeleteByParents(Integer id) {
    	dao.commentDeleteByParents(id);
    }


	

}
