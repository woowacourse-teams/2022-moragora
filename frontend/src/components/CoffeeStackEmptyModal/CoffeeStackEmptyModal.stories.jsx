import CoffeeStackEmptyModal from '.';

export default {
  title: 'Components/CoffeeStackEmptyModal',
  component: CoffeeStackEmptyModal,
};

const Template = (args) => {
  return <CoffeeStackEmptyModal {...args} />;
};

export const Default = Template.bind({});
Default.args = {};
