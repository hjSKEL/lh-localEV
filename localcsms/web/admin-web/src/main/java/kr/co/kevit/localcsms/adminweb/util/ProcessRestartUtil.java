/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.util;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * proxy-eai / ocpp16-daemon 프로세스를 강제 종료한다(Windows 전용, 같은 로컬 박스에서 admin-web 과
 * 함께 동작한다고 가정).
 *
 * <p>
 * 두 프로세스는 Windows 서비스로 등록되어(어떤 래퍼 방식이든 — NSSM, sc create 등) 비정상 종료 시
 * 서비스 복구 정책에 따라 자동 재기동되도록 운영할 계획이다. 그래서 여기서는 일부러 정상 종료
 * (graceful shutdown)가 아니라 강제 kill 만 수행한다 — graceful stop 은 서비스 매니저가 "의도된
 * 정지"로 인식해 자동 재기동을 트리거하지 않을 수 있기 때문이다.
 * </p>
 *
 * <p>
 * 프로세스 식별은 서비스명/PID 가 아니라 실행 중인 jar 파일명({@code proxy-eai.jar},
 * {@code ocpp16-daemon.jar})으로 한다 — 어떤 방식으로 서비스 등록했든 실제 java 프로세스의
 * 커맨드라인에는 항상 이 jar 이름이 포함되므로 배포 방식에 무관하게 동작한다.
 * </p>
 *
 * @since 2026. 9. 19.
 */
public class ProcessRestartUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessRestartUtil.class);

    private static final List<String> TARGET_JAR_NAMES = List.of("proxy-eai.jar", "ocpp16-daemon.jar");

    private ProcessRestartUtil() {
    }

    /** proxy-eai, ocpp16-daemon 프로세스를 각각 강제 종료 시도. 개별 실패는 로그만 남기고 나머지는 계속 진행. */
    public static void killProxyEaiAndDaemon() {
        for (String jarName : TARGET_JAR_NAMES) {
            try {
                killByJarName(jarName);
            } catch (Exception e) {
                LOGGER.error("[재가동] 프로세스 강제종료 실패 jar={}: {}", jarName, e.getMessage(), e);
            }
        }
    }

    private static void killByJarName(String jarName) throws Exception {
        String psCommand = "Get-CimInstance Win32_Process | Where-Object { $_.CommandLine -like '*" + jarName
                + "*' } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force }";
        ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-NoProfile", "-NonInteractive",
                "-Command", psCommand);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        boolean finished = process.waitFor(10, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            LOGGER.warn("[재가동] jar={} 강제종료 명령 타임아웃", jarName);
            return;
        }
        LOGGER.info("[재가동] jar={} 강제종료 명령 실행 완료(exit={}) output={}", jarName, process.exitValue(), output.trim());
    }
}
