import Header from '.';

export default {
  title: 'Components/Header',
  component: Header,
};

const Template = (args) => {
  return <Header {...args} />;
};

export const Default = Template.bind({});

export const Landing = Template.bind({});
Landing.parameters = {
  reactRouter: {
    routePath: '/meeting',
  },
};
