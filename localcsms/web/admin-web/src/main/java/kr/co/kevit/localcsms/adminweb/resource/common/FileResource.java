/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.util.WebFileUtil;
import kr.co.kevit.localcsms.adminweb.view.CommonDownloadView;
import kr.co.kevit.localcsms.common.domain.FrameworkFile;
import kr.co.kevit.localcsms.common.domain.ManagementFile;
import kr.co.kevit.localcsms.common.entity.FileManagementProvider;
import kr.co.kevit.localcsms.common.process.FileManagementService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

@RestController
@RequestMapping("ws/file")
public class FileResource extends AbstractResource{

    @Autowired
    private FileManagementService service;

    /**
     * 파일 임시 저장소 저장
     * 
     * <pre>
     * 파일 임시저장 후, 각각의 서비스 로직에서 저장소 등록 로직 적용 필요. {@link FileManagementProvider} 참고.
     * </prd>
     * 
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public FrameworkFile fileUpload(@RequestParam("file") MultipartFile file) {

        WebFileUtil fileUtil = WebFileUtil.getInstance();
        return fileUtil.storeFileToTmpLocation(file, "register");
    }

    /**
     * 저장소 파일 Download
     * 
     * <pre>
     * 파일 다운로드. {@link FrameworkFile}의 Stroage & name 사용 </prd>
     * 
     * @param file
     * @return
     */
    @RequestMapping(value = "/download")
    public CommonDownloadView downloadFile(ManagementFile file) {
        ManagementFile downloadFile = service.retrieveManagementFileById(file.getId());
        downloadFile.setStorage(service.retrieveFileStorage(downloadFile.getId(), downloadFile.getModifySeq()));
        return new CommonDownloadView(downloadFile);
    }

    /**
     * OCPP 저장소
     * 
     * @param csUniqId
     * @param file
     * @return
     */
    @RequestMapping(value = "/ocpp/upload/{csUniqId}", method = RequestMethod.POST)
    public FrameworkFile fileUpload4checkList(@PathVariable("csUniqId") String csUniqId,@RequestParam("file") MultipartFile file) {
        //
        WebFileUtil fileUtil = WebFileUtil.getInstance();
        StringBuffer buffer = new StringBuffer(256);
        buffer.append(csUniqId);
        buffer.append(StringConstants.UNDER_LINE);
        buffer.append(DateUtils.getCurrentDateAsString(DateUtils.YYYYMMDDHHMMSSSSS));
        buffer.append(StringConstants.DOT);
        buffer.append(file.getOriginalFilename());
        String newFileName = buffer.toString();
        return fileUtil.storeFileToOcppLocation(file, csUniqId, newFileName);
    }
}
