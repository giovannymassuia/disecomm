module.exports = {
  basePath: '/disecomm',
  title: 'Disecomm Event Catalog',
  tagline: 'Discover and Explore the Event Catalog',
  organizationName: 'Disecomm',
  projectName: 'Event Catalog',
  // editUrl: 'https://github.com/giovannymassuia/disecomm/edit/main/docs/events-catalog/',
  trailingSlash: true,
  primaryCTA: {
    label: 'Explore Events',
    href: '/events'
  },
  secondaryCTA: {
    label: 'Getting Started',
    href: "https://github.com/giovannymassuia/disecomm"
  },
  logo: {
    alt: 'EventCatalog Logo',
    // found in the public dir
    src: 'logo.svg',
  },
  headerLinks: [
    {label: 'Events', href: '/events'},
    {label: 'Services', href: '/services'},
    {label: 'Domains', href: '/domains'},
    {label: 'Users', href: '/users'},
    {label: 'Visualiser', href: '/visualiser'},
    {label: '3D Node Graph', href: '/overview'},
  ],
  footerLinks: [
    {label: 'Events', href: '/events'},
    {label: 'Services', href: '/services'},
    {label: 'Visualiser', href: '/visualiser'},
    {label: '3D Node Graph', href: '/overview'},
    {
      label: 'GitHub',
      href: 'https://github.com/giovannymassuia/disecomm',
    }
  ],
  users: [
    {
      id: 'giovannymassuia',
      name: 'Giovanny Massuia',
      avatarUrl: 'https://github.com/giovannymassuia.png',
      role: 'Developer',
    }
  ],
}
