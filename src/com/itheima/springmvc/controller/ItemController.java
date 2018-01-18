package com.itheima.springmvc.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.itheima.springmvc.exception.MessageException;
import com.itheima.springmvc.pojo.Items;
import com.itheima.springmvc.pojo.QueryVo;
import com.itheima.springmvc.service.ItemService;

/**
 * 	商品管理
 * @author Admin
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	//入门程序第一 包类 + 类包 + 方法名
	/**
	 * 1.ModelAndView 无敌的 带着数据 返回视图路径 (不建议使用)
	 * 2.String 返回视图路径 model来带数据 官方推荐此种方式 解耦 数据和视图分离 MVC
	 * 3.void 适合ajax异步请求 json格式数据 (response)
	 * @return
	 * @throws MessageException 
	 */
	@RequestMapping(value = "/item/itemlist.action")
	public String itemList(Model model, HttpServletRequest request,HttpServletResponse response) throws MessageException {
		
 
		//从mysql中查询
		List<Items> list = itemService.selectItemList();
		
//		if(null == null) {
//			throw new MessageException("商品信息不能为空");
//		}
//		
		model.addAttribute("itemList", list);
		return "itemList";
	}
	//修改页面入参 id
	@RequestMapping(value = "/itemEdit.action")
	public ModelAndView idEdit(Integer id, HttpServletRequest request,HttpServletResponse response) {
		//Servlet时代开发
		//		String id = request.getParameter("id");
		//查询一个商品
		//		Items items = itemService.selectById(Integer.parseInt(id));
		Items items = itemService.selectById(id);

		ModelAndView mav = new ModelAndView();
		mav.addObject("item", items);
		mav.setViewName("editItem");
		return mav;
	}
	//提交修改页面入参 为Items对象 /updateitem.action
	@RequestMapping(value = "/updateitem.action")
//  public ModelAndView updateitem(Items items) {
	public String updateitem(QueryVo vo,MultipartFile pictureFile) throws Exception {
		
		//保存图片到
		String name = UUID.randomUUID().toString().replaceAll("-", "");
		//扩展名 jpg
		String ext = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
		
		pictureFile.transferTo(new File("D:\\upload\\"+name+"."+ext));
		
		vo.getItems().setPic(name+"."+ext);
		
		
		//修改
		itemService.updateItemsById(vo.getItems());

//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("success");
//		return "redirect:/item/itemlist.action";
		return "forward:/itemEdit.action?id=" + vo.getItems().getId();
	}
	//删除多个
	@RequestMapping(value = "/deletes.action")
	public ModelAndView deletes(QueryVo vo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("success");
		return mav;
	}
	//修改多个
	@RequestMapping(value = "/updates.action")
	public ModelAndView updates(QueryVo vo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("success");
		return mav;
	}

}
