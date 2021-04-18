const PomParser = require("node-pom-parser");

const getPomVersion = () => {
  const pomPath = __dirname + "/../dex-tracker-backend/pom.xml";
  const pomSettings = {
    filePath: pomPath,
  };
  const pomObj = PomParser.parsePom(pomSettings);

  return pomObj.version;
};

module.exports = {
  getPomVersion,
};
