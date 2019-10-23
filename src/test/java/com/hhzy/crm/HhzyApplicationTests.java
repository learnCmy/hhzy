package com.hhzy.crm;


/*@RunWith(SpringRunner.class)
@SpringBootTest
public class HhzyApplicationTests {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Autowired
    private SignInfoMapper signInfoMapper;

    @Test
    public void contextLoads() {
        SysUser sysUser = new SysUser();

        sysUser.setUsername("admin");
        sysUser.setPassword("123456");
        sysUser.setCreateUserId(0l);
        sysUser.setStatus(1);
        sysUser.setEmail("411488986@qq.com");
        sysUser.setMobile("13024605668");
        sysUserService.save(sysUser);



    }

    @Test
    public  void aa(){
        SignDTO signDTO = new SignDTO();
        List<SignVo> signVos = signInfoMapper.selectSignVo(signDTO);

        System.out.println(JSON.toJSONString(signVos,true));
    }

}*/

