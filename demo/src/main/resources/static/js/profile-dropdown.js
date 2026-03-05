function initProfileDropdown() {
    console.log('initProfileDropdown fired');
    const profileBtn = document.getElementById('profileBtn');
    const profileDropdown = document.getElementById('profileDropdown');

    if (profileBtn && profileDropdown) {
    console.log('profile elements found');
    profileBtn.addEventListener('click', function(e) {
        console.log('profile button clicked');
        e.preventDefault();
        e.stopPropagation();
        const isActive = profileDropdown.classList.toggle('active');
        console.log('dropdown classList after toggle:', profileDropdown.classList);
        console.log('dropdown rect:', profileDropdown.getBoundingClientRect());
        if (isActive) {
            profileDropdown.style.opacity = '1';
            profileDropdown.style.visibility = 'visible';
            profileDropdown.style.transform = 'translateY(0)';
            profileDropdown.style.display = 'block';
        } else {
            profileDropdown.style.opacity = '0';
            profileDropdown.style.visibility = 'hidden';
            profileDropdown.style.transform = 'translateY(-10px)';
            
        }
    });

    const dropdownLinks = profileDropdown.querySelectorAll('a');
    dropdownLinks.forEach(link => {
        link.addEventListener('click', function() {
        profileDropdown.classList.remove('active');
        });
    });

    document.addEventListener('click', function(e) {
        if (!profileBtn.contains(e.target) && !profileDropdown.contains(e.target)) {
        profileDropdown.classList.remove('active');
        }
    });
    }
}


if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initProfileDropdown);
} else {
    initProfileDropdown();
}


setTimeout(initProfileDropdown, 100);
