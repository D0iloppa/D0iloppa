// 기본 설정
const fs = require('fs'); // 파일로드 사용
const express = require("express"); // express 웹 서버 사용
const app = express();
const PORT = 3939;

const os = require("os");
const IP = getServerIp();
// server ip 구하기
function getServerIp() {
  var ifaces = os.networkInterfaces();
  var result = '';
  for (var dev in ifaces) {
      var alias = 0;
      ifaces[dev].forEach(function(details) {
          if (details.family == 'IPv4' && details.internal === false) {
              result = details.address;
              ++alias;
          }
      });
  }
  return result;
}

// 정적 파일 불러오기
app.use(express.static(__dirname + "/public"));

// 라우팅 정의
app.get("/", (req, res) => {
  //res.sendFile(__dirname + "/index.html");
  res.sendFile(__dirname + "/index.html");
});

// 서버 실행
app.listen(PORT, () => {
  console.log(`Listen : doil TEST server running at http://${IP}:${PORT}`);
});