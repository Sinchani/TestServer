package net.media.spamserver.services;

import cz.mallat.uasparser.fileparser.Entry;
import cz.mallat.uasparser.fileparser.PHPFileParser;
import cz.mallat.uasparser.fileparser.Section;
import net.media.spamserver.config.Config;
import net.media.spamserver.model.RobotEntry;
import net.media.spamserver.util.FileUtil;
import org.ahocorasick.trie.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class BotUaService {

    @Autowired
    private Trie tree;
    private Trie currentTree;
    private final String UA_PREFIX = "#start#";
    private Map<String, RobotEntry> robotsMap = new HashMap<String, RobotEntry>();

    private String[] getBotSpidersList(){
        String str = FileUtil.readFile(Config.BOT_SPIDERS_LIST_LOCATION);
        return str.split("\n");
    }

    private String addUserAgentPrefix(String userAgent){
        return UA_PREFIX + userAgent;
    }

    private boolean isUserAgentPresent(String userAgent){
        Collection searcher = currentTree.parseText(userAgent);
        return searcher.size() != 0;
    }

    public boolean isBotUA(String userAgent) {
        if(currentTree == null) {
            this.initializeBotUaList();
        }
        userAgent = addUserAgentPrefix(userAgent);
        return isUserAgentPresent(userAgent);
    }

    private void createInternalDataStructure(List<Section> sectionList) {
        for (Section sec : sectionList) {
            if ("robots".equals(sec.getName())) {
                Map<String, RobotEntry> robotsMapTmp = new HashMap<String, RobotEntry>();
                for (Entry en : sec.getEntries()) {
                    RobotEntry re = new RobotEntry(en.getData());
                    robotsMapTmp.put(re.getUserAgentString(), re);
                }
                robotsMap = robotsMapTmp;
            }
        }
    }

    private void loadDataFromFile(InputStream is) throws IOException {
        PHPFileParser fp = new PHPFileParser(is);
        createInternalDataStructure(fp.getSections());
    }

    public void updateBotList() {
        try {
            loadDataFromFile(FileUtil.getBotListInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        processBotData();
    }

    private void processBotData() {
        if (robotsMap.size() == 0) {
            return;
        }
        List<String> botUserAgents = new LinkedList<String>();
        for (Map.Entry<String, RobotEntry> entry : robotsMap.entrySet()) {
            botUserAgents.add(entry.getKey());
        }
        reUpdateTree(botUserAgents);
    }

    public synchronized void initializeBotUaList() {
        if (currentTree != null){
            return;
        }
        String[] lines = getBotSpidersList();
        for(int i=1; i < lines.length;i++) {
            if (lines[i].charAt(0) != '"') {
                String[] tokens = lines[i].split("\\|\\|\\|");
                if(tokens.length != 2 ) {
                    continue;
                }
                String ua = tokens[0].trim();
                String start = tokens[1].trim();
                if(start.equals("True")) {
                    ua = addUserAgentPrefix(ua);
                }
                tree.addKeyword(ua);
            }
        }
        //tree.prepare();
        currentTree = tree;
    }

    public synchronized void reUpdateTree(List<String> userAgents) {
        Trie temporaryTree = new Trie();
        for(String userAgent : userAgents) {
            temporaryTree.addKeyword(userAgent);
        }
        currentTree = temporaryTree;
    }
}