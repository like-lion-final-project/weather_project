package com.example.demo.ai.repo;

import com.example.demo.ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



// 테스트용 임시 레포 입니다. UserRepo 구현되면 삭제 해주세요.
public interface UserRepo extends JpaRepository<User,Long> {

}
