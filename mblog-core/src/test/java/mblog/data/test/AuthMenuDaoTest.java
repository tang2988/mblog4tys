package mblog.data.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import mblog.core.persist.dao.AuthMenuDao;
import mblog.core.persist.entity.AuthMenuPO;
import mblog.core.persist.service.CardTransactionRecordService;

public class AuthMenuDaoTest extends SpringTransactionalContextTests{
	
	@Autowired
	private AuthMenuDao authMenuDao;
	
	public void testFindByParentId(){
		List<AuthMenuPO> list = authMenuDao.findByParentId(2L);
		System.out.println(list);
	}
	
	@Autowired
	CardTransactionRecordService cardTransactionRecordService;
}
