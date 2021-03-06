package committee.nova.mcmodwiki.core;

import committee.nova.mcmodwiki.MSRClient;
import committee.nova.mcmodwiki.utils.Utilities;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/4/20 8:31
 * Version: 1.0
 */
public class CoreService {

    public static boolean tryOpen(ItemStack stack) {
        String modName, regName, displayName, url;
        int mcModApiNum;

        // 先进行字符获取与转义
        try {
            modName = URLEncoder.encode(Objects.requireNonNull(Utilities.getDefaultCreatorModId(stack)), StandardCharsets.UTF_8);
            regName = URLEncoder.encode(Objects.requireNonNull(Utilities.getIdentifier(stack.getItem().getTranslationKey())), StandardCharsets.UTF_8);
            displayName = URLEncoder.encode(stack.getName().getString(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // 访问 mcmod 百科 api，获取物品对应 id
        try {
            final var apiUrl = new URL(String.format("https://api.mcmod.cn/getItem/?regname=%s", regName));
            mcModApiNum = Integer.parseInt(IOUtils.readLines(apiUrl.openStream(), StandardCharsets.UTF_8).get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // 通过获取的 id 判定生成什么连接
        // 有则去往对应物品，无则尝试进行搜索
        url = mcModApiNum > 0 ? String.format("https://www.mcmod.cn/item/%d.html", mcModApiNum) : String.format("https://search.mcmod.cn/s?key=%s+%s", modName, displayName);

        // 打开对应连接
        try {
            browse(url);
        } catch (Exception e) {
            MSRClient.LOGGER.error("打开链接失败", e);
            return false;
        }
        return true;
    }

    private static void browse(String url) throws Exception {
        // 获取操作系统的名字
        final var osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            // 苹果的打开方式
            final var fileMgr = Class.forName("com.apple.eio.FileManager");
            final var openURL = fileMgr.getDeclaredMethod("openURL",
                    String.class);
            openURL.invoke(null, url);
        } else if (osName.startsWith("Windows")) {
            // windows的打开方式。
            Runtime.getRuntime().exec(
                    "rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            // Unix or Linux的打开方式
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany",
                    "mozilla", "netscape"};
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
                // 执行代码，在browser有值后跳出，
                // 这里是如果进程创建成功了，==0是表示正常结束。
                if (Runtime.getRuntime()
                        .exec(new String[]{"which", browsers[count]})
                        .waitFor() == 0)
                    browser = browsers[count];
            if (browser == null)
                throw new Exception("Could not find web browser");
            else
                // 这个值在上面已经成功地得到了一个进程。
                Runtime.getRuntime().exec(new String[]{browser, url});
        }
    }


}
