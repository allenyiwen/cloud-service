package com.cloud.file.controller;

import com.cloud.common.utils.PageUtil;
import com.cloud.file.config.FileServiceFactory;
import com.cloud.file.dao.FileDao;
import com.cloud.file.model.FileInfo;
import com.cloud.file.service.FileService;
import com.cloud.model.common.Page;
import com.cloud.model.log.LogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

	@Autowired
	private FileServiceFactory fileServiceFactory;

	/**
	 * 文件上传<br>
	 * 根据fileSource选择上传方式，目前仅实现了上传到本地<br>
	 * 如有需要可上传到第三方，如阿里云、七牛等
	 * 
	 * @param file
	 * @param fileSource
	 *            FileSource
	 * 
	 * @return
	 * @throws Exception
	 */
	@LogAnnotation(module = "文件上传", recordParam = false)
	@PostMapping
	public FileInfo upload(@RequestParam("file") MultipartFile file, String fileSource) throws Exception {
		FileService fileService = fileServiceFactory.getFileService(fileSource);
		return fileService.upload(file);
	}

	/**
	 * layui富文本文件自定义上传
	 * 
	 * @param file
	 * @param fileSource
	 * @return
	 * @throws Exception
	 */
	@LogAnnotation(module = "文件上传", recordParam = false)
	@PostMapping("/layui")
	public Map<String, Object> uploadLayui(@RequestParam("file") MultipartFile file, String fileSource)
			throws Exception {
		FileInfo fileInfo = upload(file, fileSource);

		Map<String, Object> map = new HashMap<>();
		map.put("code", 0);
		Map<String, Object> data = new HashMap<>();
		data.put("src", fileInfo.getUrl());
		map.put("data", data);

		return map;
	}

	/**
	 * 文件删除
	 * 
	 * @param id
	 */
	@LogAnnotation(module = "文件删除")
	@PreAuthorize("hasAuthority('file:del')")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		FileInfo fileInfo = fileDao.getById(id);
		if (fileInfo != null) {
			FileService fileService = fileServiceFactory.getFileService(fileInfo.getSource());
			fileService.delete(fileInfo);
		}
	}

	@Autowired
	private FileDao fileDao;

	/**
	 * 文件查询
	 * 
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('file:query')")
	@GetMapping
	public Page<FileInfo> findFiles(@RequestParam Map<String, Object> params) {
		int total = fileDao.count(params);
		List<FileInfo> list = Collections.emptyList();
		if (total > 0) {
			PageUtil.pageParamConver(params, true);

			list = fileDao.findData(params);
		}
		return new Page<>(total, list);
	}
}
