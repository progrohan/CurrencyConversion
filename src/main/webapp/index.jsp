<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Happy New Year!</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(to bottom, #1e3c72, #2a5298);
            color: white;
            text-align: center;
            overflow: hidden;
        }
        h1 {
            margin-top: 50px;
            font-size: 4em;
        }
        .wishes {
            font-size: 2em;
            margin: 20px 0;
        }
        .snowflake {
            position: absolute;
            top: -10px;
            animation: fall linear infinite;
        }
        .tree {
            font-size: 2em;
            margin: 20px;
        }
        .landscape {
            position: absolute;
            bottom: 0;
            width: 100%;
            height: 100px;
            background: linear-gradient(to top, #003300, #004d00);
            display: flex;
            justify-content: center;
            align-items: flex-end;
            padding-bottom: 10px;
        }
        .landscape .tree {
            margin: 0 10px;
        }
        @keyframes fall {
            to {
                transform: translateY(100vh);
            }
        }
    </style>
</head>
<body>
<h1>🎉 Happy New Year 2025! 🎊</h1>
<div class="wishes">Wishing you joy, health, and prosperity in the New Year!</div>

<!-- Ёлочки -->
<div class="tree">🎄🎄🎄🎄🎄</div>

<!-- Новогодний пейзаж -->
<div class="landscape">
    <div class="tree">🎄</div>
    <div class="tree">🎄</div>
    <div class="tree">🎄</div>
    <div class="tree">🎄</div>
    <div class="tree">🎄</div>
</div>

<script>
    // Snowfall Effect
    function createSnowflakes() {
        const snowflake = document.createElement('div');
        snowflake.classList.add('snowflake');
        snowflake.innerHTML = '❅';
        snowflake.style.left = `${Math.random() * 100}vw`;
        snowflake.style.animationDuration = `${Math.random() * 3 + 2}s`;
        snowflake.style.fontSize = `${Math.random() * 20 + 10}px`;
        document.body.appendChild(snowflake);

        setTimeout(() => {
            snowflake.remove();
        }, 5000);
    }

    setInterval(createSnowflakes, 200);
</script>
</body>
</html>
