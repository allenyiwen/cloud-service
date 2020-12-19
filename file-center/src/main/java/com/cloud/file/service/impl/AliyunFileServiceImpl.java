package com.cloud.file.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.cloud.file.dao.FileDao;
import com.cloud.file.model.FileInfo;
import com.cloud.file.model.FileSource;

/**
 * 阿里云存储文件
 * 
 * @author allen zhangxuelun@cmsr.chinamobile.com
 *
 */
@Service("aliyunFileServiceImpl")
public class AliyunFileServiceImpl extends AbstractFileService {

	@Autowired
	private FileDao fileDao;

	@Override
	protected FileDao getFileDao() {
		return fileDao;
	}

	@Override
	protected FileSource fileSource() {
		return FileSource.ALIYUN;
	}

	@Autowired
	private OSSClient ossClient;

	@Value("${file.aliyun.bucketName}")
	private String bucketName;
	@Value("${file.aliyun.domain}")
	private String domain;

	@Override
	protected void uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception {
		ossClient.putObject(bucketName, fileInfo.getName(), file.getInputStream());
		fileInfo.setUrl(domain + "/" + fileInfo.getName());
	}

	@Override
	protected boolean deleteFile(FileInfo fileInfo) {
		ossClient.deleteObject(bucketName, fileInfo.getName());
		return true;
	}

}
