import {app, BrowserWindow, ipcMain} from 'electron'
import * as path from "node:path";

function createWin() {
    const win = new BrowserWindow({
        width: 800,
        height: 600,
        show: false,
        autoHideMenuBar: true,
        webPreferences: {
            preload: path.join(__dirname, './preload.js'),
            sandbox: false,
        }
    })

    win.on("ready-to-show", () => {
        win.show();
        win.webContents.openDevTools()
    })

    if (process.env.VITE_DEV_SERVER_URL) {
        win.loadURL(process.env.VITE_DEV_SERVER_URL)
    } else {
        win.loadFile('dist/index.html');
    }
}


app.whenReady().then(() => {
    ipcMain.on('ping', () => console.log('pong'))

    createWin();
    app.on('activate', function () {
        // mac特定处理
        if (BrowserWindow.getAllWindows().length === 0) createWin()
    })
})

// mac特定处理
app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit()
    }
})

// 下面可以包括主进程特定代码的其余部分。也可以将它们放在单独的文件中，并在这里引入。