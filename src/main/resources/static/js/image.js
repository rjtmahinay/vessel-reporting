
$(":file").filestyle();

$('.img-wrap .close').on('click', function() {
    var id = $(this).closest('.img-wrap').find('img').data('id');
    $(".media-inputs").append('<input type="hidden" class="form-control" name="media_id[]" id="media_id" value="'+ id + '">');
    
    var imageItself = document.getElementById('div_medium' + id);
    imageItself.parentNode.removeChild(imageItself);
});