import MeetingItemSkeleton from '.';

export default {
  title: 'Components/MeetingItemSkeleton',
  component: MeetingItemSkeleton,
};

const Template = (args) => {
  return <MeetingItemSkeleton {...args} />;
};

export const Default = Template.bind({});
