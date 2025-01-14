<?php require_once 'includes/config.php'; ?>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Instituto de Caridade - Início</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css">
</head>
<body>
    <!-- Navbar existente... -->

    <!-- Novo Slider -->
    <div class="swiper-container mb-4">
        <div class="swiper-wrapper">
            <div class="swiper-slide">
                <div class="slide-content" style="background-image: url('assets/img/slider/slide1.jpg');">
                    <div class="slide-overlay">
                        <div class="container">
                            <h2>Ajude Quem Precisa</h2>
                            <p>Sua doação pode transformar vidas</p>
                            <a href="doacoes/cadastro.php" class="btn btn-light btn-lg">Doe Agora</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="slide-content" style="background-image: url('assets/img/slider/slide2.jpg');">
                    <div class="slide-overlay">
                        <div class="container">
                            <h2>Seja um Parceiro</h2>
                            <p>Junte-se a nós nesta causa</p>
                            <a href="empresas/cadastro.php" class="btn btn-light btn-lg">Cadastre sua Empresa</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="slide-content" style="background-image: url('assets/img/slider/slide3.jpg');">
                    <div class="slide-overlay">
                        <div class="container">
                            <h2>Impacto Social</h2>
                            <p>Conheça as famílias beneficiadas</p>
                            <a href="familias/listar.php" class="btn btn-light btn-lg">Saiba Mais</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="swiper-pagination"></div>
        <div class="swiper-button-next"></div>
        <div class="swiper-button-prev"></div>
    </div>

    <!-- Resto do conteúdo existente... -->

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
    <script>
        var swiper = new Swiper('.swiper-container', {
            slidesPerView: 1,
            spaceBetween: 0,
            loop: true,
            autoplay: {
                delay: 5000,
                disableOnInteraction: false,
            },
            pagination: {
                el: '.swiper-pagination',
                clickable: true,
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },
        });
    </script>
</body>
</html> 