const path = require("path");

const publicPath = (process.env.NODE_ENV === "production" && process.env.VERCEL !== "true") ? "/siga-le/" : "/";
const outputDir = (process.env.NODE_ENV === "production" && process.env.VERCEL === "true") ? "dist" : "../webapp";


module.exports = {
  outputDir: path.resolve(__dirname, outputDir),
  assetsDir: "static",
  publicPath: publicPath,
  lintOnSave: true,
  runtimeCompiler: true,
  pwa: {
    name: "Siga-Le",
    themeColor: "#058fcc",
    msTileColor: "#058fcc",
    appleMobileWebAppCapable: "yes",
    appleMobileWebAppStatusBarStyle: "black",
    assetsVersion: "",
    manifestOptions: {
      short_name: "Siga-Le",
      scope: "/",
      start_url: publicPath + "index.html",
      description: "Siga-Doc UI",
      background_color: "#058fcc",
      "theme-color": "#058fcc"
    },

    // configure the workbox plugin
    workboxPluginMode: "GenerateSW",
    workboxOptions: {
      skipWaiting: true
    },
    iconPaths: {
      favicon32: "img/icons/favicon-32x32.png",
      favicon16: "img/icons/favicon-16x16.png",
      appleTouchIcon: "img/icons/apple-touch-icon-152x152.png",
      maskIcon: "img/icons/safari-pinned-tab.svg",
      msTileImage: "img/icons/msapplication-icon-144x144.png"
    }
  },
  devServer: {
    disableHostCheck: true
  }
};