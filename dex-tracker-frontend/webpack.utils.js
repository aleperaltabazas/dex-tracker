const fs = require("fs");

const getPomVersion = () => {
  const pomPath = __dirname + "/../dex-tracker-backend/pom.xml";
  const content = fs.readFileSync(pomPath, "utf8");
  const match = content.match(/<version>([^<]+)<\/version>/);
  if (!match) throw new Error("Could not find <version> in pom.xml");
  return match[1];
};

module.exports = {
  getPomVersion,
};
